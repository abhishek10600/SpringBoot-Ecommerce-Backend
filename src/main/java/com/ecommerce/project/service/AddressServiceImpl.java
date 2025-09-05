package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.repositories.AddressRepository;
import com.ecommerce.project.repositories.UserRepository;
import com.ecommerce.project.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    AuthUtil authUtil;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {

        Address address = modelMapper.map(addressDTO, Address.class);
        List<Address> addressList = user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);

        address.setUser(user);
        Address savedAddress = addressRepository.save(address);

        AddressDTO savedAddressDTO = modelMapper.map(savedAddress, AddressDTO.class);
        return savedAddressDTO;
    }

    @Override
    public List<AddressDTO> getAddresses() {

        List<Address> addresses = addressRepository.findAll();
        List<AddressDTO> addressesDTO = addresses.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .toList();

        return addressesDTO;
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {

        Address address = addressRepository.findById(addressId).orElseThrow(()-> new ResourceNotFoundException("Address", "addressId", addressId));
        AddressDTO addressDTO = modelMapper.map(address, AddressDTO.class);
        return addressDTO;
    }

    @Override
    public List<AddressDTO> getUserAddresses(User user) {
        List<Address> addresses = user.getAddresses();
        List<AddressDTO> addressesDTO = addresses.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class)).toList();
        return addressesDTO;
    }
}
